#!/usr/bin/env bash
set -euo pipefail

API_URL="${SWITCHBOARD_API_URL:-http://localhost:8080}"

post() {
    local code
    code=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$API_URL$1" \
        -H "Content-Type: application/json" \
        -d "$2")
    if [ "$code" = "201" ]; then
        return 0
    elif [ "$code" = "409" ] || [ "$code" = "500" ]; then
        return 1
    else
        echo "  unexpected response: HTTP $code on POST $1"
        return 1
    fi
}

echo "switchboard setup"
echo "================="
echo ""
echo "api: $API_URL"
echo ""

echo "waiting for server..."
for i in $(seq 1 30); do
    if curl -sf "$API_URL/actuator/health" > /dev/null 2>&1; then
        echo "server is up"
        break
    fi
    if [ "$i" -eq 30 ]; then
        echo "error: server not reachable at $API_URL after 30s"
        exit 1
    fi
    sleep 1
done

echo ""

echo "creating organization..."
ORG_RESPONSE=$(curl -s -X POST "$API_URL/api/v1/organizations" \
    -H "Content-Type: application/json" \
    -d '{"name":"Default Organization","slug":"default"}')
ORG_ID=$(echo "$ORG_RESPONSE" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)
if [ -z "$ORG_ID" ]; then
    ORG_ID=$(curl -s "$API_URL/api/v1/organizations" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)
    echo "  org: $ORG_ID (already exists)"
else
    echo "  org: $ORG_ID"
fi

echo "creating project..."
post "/api/v1/projects" "{\"organizationId\":\"$ORG_ID\",\"name\":\"Demo Project\",\"key\":\"demo-project\"}" \
    && echo "  project: demo-project" \
    || echo "  project: demo-project (already exists)"

echo "creating environments..."
for env in '{"name":"Development","key":"dev","sortOrder":0}' \
           '{"name":"Staging","key":"staging","sortOrder":1}' \
           '{"name":"Production","key":"production","sortOrder":2}'; do
    post "/api/v1/projects/demo-project/environments" "$env" || true
done
echo "  environments: dev, staging, production"

echo "creating sample flags..."
for flag in \
    '{"key":"new-checkout","name":"New Checkout Flow","description":"Redesigned checkout experience","flagType":"RELEASE","defaultVariant":"off","variants":[{"key":"on","value":"true"},{"key":"off","value":"false"}]}' \
    '{"key":"dark-mode","name":"Dark Mode","description":"Enable dark mode UI theme","flagType":"RELEASE","defaultVariant":"off","variants":[{"key":"on","value":"true"},{"key":"off","value":"false"}]}' \
    '{"key":"premium-dashboard","name":"Premium Dashboard","description":"Premium-only dashboard features","flagType":"PERMISSION","defaultVariant":"off","variants":[{"key":"on","value":"true"},{"key":"off","value":"false"}]}'; do
    post "/api/v1/projects/demo-project/flags" "$flag" || true
done
echo "  flags: new-checkout, dark-mode, premium-dashboard"

echo "enabling flags in dev..."
curl -s -X PATCH "$API_URL/api/v1/projects/demo-project/flags/new-checkout/environments/dev/toggle" > /dev/null || true
curl -s -X PATCH "$API_URL/api/v1/projects/demo-project/flags/dark-mode/environments/dev/toggle" > /dev/null || true
echo "  toggled: new-checkout, dark-mode"

echo "setting rollout in staging..."
curl -s -X PATCH "$API_URL/api/v1/projects/demo-project/flags/new-checkout/environments/staging/toggle" > /dev/null || true
curl -s -X PUT "$API_URL/api/v1/projects/demo-project/flags/new-checkout/environments/staging/rollout" \
    -H "Content-Type: application/json" \
    -d '{"percentage":50}' > /dev/null || true
echo "  new-checkout: 50% rollout in staging"

echo ""
echo "setup complete!"
echo ""
echo "  dashboard:  http://localhost:5173"
echo "  swagger:    $API_URL/swagger-ui.html"
echo "  api:        $API_URL/api/v1/projects/demo-project/flags"
echo ""
echo "try it:"
echo "  curl $API_URL/api/v1/projects/demo-project/flags"
echo "  curl -X PATCH $API_URL/api/v1/projects/demo-project/flags/premium-dashboard/environments/dev/toggle"

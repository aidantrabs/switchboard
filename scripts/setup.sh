#!/usr/bin/env bash
set -euo pipefail

API_URL="${SWITCHBOARD_API_URL:-http://localhost:8080}"

echo "switchboard setup"
echo "================="
echo ""
echo "api: $API_URL"
echo ""

# wait for server to be ready
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

# create organization
echo "creating organization..."
ORG_RESPONSE=$(curl -sf -X POST "$API_URL/api/v1/organizations" \
    -H "Content-Type: application/json" \
    -d '{"name":"Default Organization","slug":"default"}')
ORG_ID=$(echo "$ORG_RESPONSE" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)
echo "  org: $ORG_ID"

# create project
echo "creating project..."
curl -sf -X POST "$API_URL/api/v1/projects" \
    -H "Content-Type: application/json" \
    -d "{\"organizationId\":\"$ORG_ID\",\"name\":\"Demo Project\",\"key\":\"demo-project\"}" > /dev/null
echo "  project: demo-project"

# create environments
echo "creating environments..."
for env in '{"name":"Development","key":"dev","sortOrder":0}' \
           '{"name":"Staging","key":"staging","sortOrder":1}' \
           '{"name":"Production","key":"production","sortOrder":2}'; do
    curl -sf -X POST "$API_URL/api/v1/projects/demo-project/environments" \
        -H "Content-Type: application/json" \
        -d "$env" > /dev/null
done
echo "  environments: dev, staging, production"

# create sample flags
echo "creating sample flags..."

curl -sf -X POST "$API_URL/api/v1/projects/demo-project/flags" \
    -H "Content-Type: application/json" \
    -d '{
        "key":"new-checkout","name":"New Checkout Flow",
        "description":"Redesigned checkout experience",
        "flagType":"RELEASE","defaultVariant":"off",
        "variants":[{"key":"on","value":"true"},{"key":"off","value":"false"}]
    }' > /dev/null

curl -sf -X POST "$API_URL/api/v1/projects/demo-project/flags" \
    -H "Content-Type: application/json" \
    -d '{
        "key":"dark-mode","name":"Dark Mode",
        "description":"Enable dark mode UI theme",
        "flagType":"RELEASE","defaultVariant":"off",
        "variants":[{"key":"on","value":"true"},{"key":"off","value":"false"}]
    }' > /dev/null

curl -sf -X POST "$API_URL/api/v1/projects/demo-project/flags" \
    -H "Content-Type: application/json" \
    -d '{
        "key":"premium-dashboard","name":"Premium Dashboard",
        "description":"Premium-only dashboard features",
        "flagType":"PERMISSION","defaultVariant":"off",
        "variants":[{"key":"on","value":"true"},{"key":"off","value":"false"}]
    }' > /dev/null

echo "  flags: new-checkout, dark-mode, premium-dashboard"

# toggle some flags on in dev
echo "enabling flags in dev..."
curl -sf -X PATCH "$API_URL/api/v1/projects/demo-project/flags/new-checkout/environments/dev/toggle" > /dev/null
curl -sf -X PATCH "$API_URL/api/v1/projects/demo-project/flags/dark-mode/environments/dev/toggle" > /dev/null
echo "  enabled: new-checkout, dark-mode"

# set a rollout in staging
echo "setting rollout in staging..."
curl -sf -X PATCH "$API_URL/api/v1/projects/demo-project/flags/new-checkout/environments/staging/toggle" > /dev/null
curl -sf -X PUT "$API_URL/api/v1/projects/demo-project/flags/new-checkout/environments/staging/rollout" \
    -H "Content-Type: application/json" \
    -d '{"percentage":50}' > /dev/null
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

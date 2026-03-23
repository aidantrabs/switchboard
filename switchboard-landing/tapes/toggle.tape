Output ../public/demo/toggle.gif
Set Theme "Catppuccin Mocha"
Set FontFamily "JetBrains Mono"
Set FontSize 14
Set Width 900
Set Height 500
Set Padding 20

Type "# list flags in dev environment"
Enter
Sleep 500ms

Type "curl -s localhost:8080/api/v1/projects/demo-project/flags | jq .[].key"
Enter
Sleep 2s

Type "# toggle premium-dashboard on in dev"
Enter
Sleep 500ms

Type "curl -s -X PATCH localhost:8080/api/v1/projects/demo-project/flags/premium-dashboard/environments/dev/toggle"
Enter
Sleep 2s

Type "# verify — check all flags in dev"
Enter
Sleep 500ms

Type "curl -s localhost:8080/api/v1/client/demo-project/dev/flags | jq .[].key"
Enter
Sleep 2s

Type "# toggle it back off"
Enter
Sleep 500ms

Type "curl -s -X PATCH localhost:8080/api/v1/projects/demo-project/flags/premium-dashboard/environments/dev/toggle"
Enter
Sleep 2s

Output ../public/demo/sdk.gif
Set Theme "Catppuccin Mocha"
Set FontFamily "JetBrains Mono"
Set FontSize 14
Set Width 900
Set Height 500
Set Padding 20

Type "# check flag state via demo service"
Enter
Sleep 500ms

Type "curl -s 'localhost:8081/demo?userId=user-1' | jq"
Enter
Sleep 2s

Type "# toggle the flag off"
Enter
Sleep 500ms

Type "curl -s -X PATCH localhost:8080/api/v1/projects/demo-project/flags/new-checkout/environments/dev/toggle"
Enter
Sleep 2s

Type "# wait for sdk poll (5s)"
Enter
Sleep 500ms

Type "sleep 6"
Enter
Sleep 6s

Type "# check again - flag is now off"
Enter
Sleep 500ms

Type "curl -s 'localhost:8081/demo?userId=user-1' | jq"
Enter
Sleep 3s

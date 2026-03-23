Output ../public/demo/cli.gif
Set Theme "Catppuccin Mocha"
Set FontFamily "JetBrains Mono"
Set FontSize 14
Set Width 900
Set Height 500
Set Padding 20

Type "# list all flags"
Enter
Sleep 500ms

Type "make cli ARGS='flags list --project demo-project'"
Enter
Sleep 3s

Type "# toggle a flag"
Enter
Sleep 500ms

Type "make cli ARGS='flags toggle new-checkout --env dev --project demo-project'"
Enter
Sleep 2s

Type "# check flag detail"
Enter
Sleep 500ms

Type "make cli ARGS='flags get new-checkout --project demo-project'"
Enter
Sleep 3s

Type "# view audit log"
Enter
Sleep 500ms

Type "make cli ARGS='audit --project demo-project --last 5'"
Enter
Sleep 3s

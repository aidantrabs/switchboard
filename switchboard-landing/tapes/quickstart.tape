Output ../public/demo/quickstart.gif
Set Theme "Catppuccin Mocha"
Set FontFamily "JetBrains Mono"
Set FontSize 14
Set Width 900
Set Height 500
Set Padding 20

Type "# clone and start switchboard"
Enter
Sleep 500ms

Type "git clone https://github.com/aidantrabs/switchboard.git"
Enter
Sleep 2s

Type "cd switchboard"
Enter
Sleep 500ms

Type "make dev"
Enter
Sleep 3s

Type "# server is running on localhost:8080"
Enter
Sleep 1s

Type "# seed sample data"
Enter
Sleep 500ms

Type "make seed"
Enter
Sleep 3s

Type "# done! open the dashboard"
Enter
Sleep 500ms

Type "open http://localhost:5173"
Enter
Sleep 2s

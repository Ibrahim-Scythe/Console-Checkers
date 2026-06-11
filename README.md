## Console Checkers
A 2 Player Checkers game to be played on the command line.
Written entirely in Java.

### Compilation
- Requires JDK 17+
- Clone the repository.
Then from the root directory, run the command:
```bash
javac src/*.java
```

### Running the game
From the root directory, run either:
#### GUI
```bash
java -cp src Main
```
#### CLI
Windows:
```bash
chcp 65001
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
java -cp src Main -cli
```
Other: 
```bash
java -cp src Main -cli
```
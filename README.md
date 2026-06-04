## Console Checkers
A 2 Player Checkers game to be played on the command line.
Written entirely in Java.

### Compilation
- Requires JDK 17+
- Clone the repository or download the `src` folder.
Then to compile all the class files, run the command:
```bash
cd src
javac *.java
```

### Running the game
In the same folder where the files were compiled run:
```bash
java Main
```
To prevent the board from flipping every turn use parameter `-noflip` e.g.
```bash
java Main -noflip
```
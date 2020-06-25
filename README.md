# Debugger javac plugin

# What is it

It helps speed up conditional breakpoint in your java program.

# Using

1. Build plugin:
```./gradlew build jar```
2. Compile your file:
`javac -Xplugin:"DebuggerPlugin <full path to your java file>:<line number>:<condition>" -g -cp ./build/libs/javac-plugin-1.0-SNAPSHOT.jar <path to compilable file>>`
3. Run your .class file in any debugger

# Notes
Breakpointed line must be wrapped by empty line on both sides

For example:
If you want to put breakpoint in line 3, lines 2 and 4 must be empty
```
1: int x = 3;
2: --
3: System.out.println(x);
4: --
```

# Gradle + intellij idea

TODO
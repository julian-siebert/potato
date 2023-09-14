# Potato
Potato makes it possible to compile, load and run *.java*-files at runtime.

It's also possible to reload your classes, if you don't use them anymore.

## How it works
Potato grabs a non-compiled source-directory with one class innit.
This class then gets compiled to a *.class*-file temporarily. 
After compiling the ClassLoader of Potato instantly loads
and initializes the class. The class implements the `PotatoScript` interface. 
Within this interface you have three different methods
to whether `start`, `stop` or `kill` the script. 
They can be called by the Potato instance and that's pretty much how everything works.

## Simple example setup
Let's imagine we have a source-directory `./scripts/de/exampleorganisation/` in our programs environment.
Within this directory we have a `Test.java` file.

Our `Test.java` file looks like following:

```java
package de.exampleorganisation;

import de.juliansiebert.potato.PotatoScript;

public final class Test implements PotatoScript {

    @Override
    public void start() {
        //You can call this method, if you want to start this script.
        System.out.println("Hello world! :D");
    }

    @Override
    public void stop() {
        //You can call this method, if you want to stop this script.
        System.out.println("Bye world! :(");
    }
    
    @Override
    public void kill() {
        /*
         Called when all object references of this class should be deleted. 
         Otherwise, you could get in trouble with memory, because the GC won't remove this instance.
         Sadly it's not possible to remove it by yourself in Java without a custom build JRE.
         */
        System.out.println("NOOOOO world! >:o");
    }

    @Override
    public String getName() {
        return "TestScript"; // This is the scripts name. It's used to identify this script. 
    }

    @Override
    public String getVersion() {
        return "ExampleVersionNumber"; // Ignored by potato. You can use it if you want to.
    }
}
```

Now we can use following code in our program to run this script.

Following shows an example:

```java
public static void main(String... args) {
    Potato potato = new Potato();
    
    /* The first parameter "TestScript" must match the return type of the PotatoScript#getName method.
       2nd parameter is the full class name WITHOUT the `.java` extension.
       3rd parameter is just the path of the scripts as mentioned.
     */
    PotatoScript exampleScript = potato.loadScript("TestScript", "de/exampleorganisation/Test", "./scripts");
    
    exampleScript.start();
    
    potato.unloadScript(exampleScript); // Get rid of the script. Also calls the PotatoScript#kill method.
    exampleScript.start();
}
```
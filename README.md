# Food truck info fetcher

Foobar is a Python library for dealing with word pluralization.

## Requirements

Install java 14+ as per 

## Installation

Download the zip file, extract it and cd into it

```bash
wget URL
unzip 
cd 
```



## Usage

Just execute:

```bash
sh -x mvnw compile exec:java -Dexec.mainClass="com.task.oracte.OracteApplication"
```

This should be enough to download dependencies, compile and run the main class, you should see something like:


```bash
  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                                                                                                                                                    
  @@@@@@@@@@:@@@@@:@@@&#@@@@@@@@@8@@@:@@@:@@@:@@@o@@@@@@@@@@@@@@@@@@@@@@@@@@@@                                                                                                                                                    
  @@@@@@@@@@:@@@@@:@@@&#@@@@@@@@@8@#@:@@@:@@@::@@@:@@@@@@@@@@@@@@@@@@@@@@@@@@@                                                                                                                                                    
  @@@@@@@@@@:@@@@@:@@@&#@@@@@@@@@8@@@:@@@:@@@::@@@@:@@@@@@@@@@@@@@@@@@@@@@@@@@                                                                                                                                                    
  @@@@@@@@@@:@@@o@o@8@##@#@@@@@@@8@@@&&:@:@:@:@:@:@:@@@@@@@@@@@@@@@@@@@@@@@@@@                                                                                                                                                    
  @@@@:&&&::**********************************:::::::::::::@@@@@@@@@@@@@@@@@@@                                                                                                                                                    
  @@@@::::::              :              ::::::::*.........:@@@@@@@@@@@@@@@@@@                                                                                                                                                    
  @@@@::::::        ....  :        ...   ::::::::      .    :@@@@@@@@@@@@@@@@@                                                                                                                                                    
  @@@@::::::       .. .   :       .. .   ::::::::    ...     :@@@@@@@@@@@@@@@@                                                                                                                                                    
  @@@@::::::     .. .     :     ....     ::::::::   ...      ..@@@@@@@@@@@@@@@                                                                                                                                                    
  @@@@::::::   ....       :   ...        ::::::::  .        ....@@@@@@@@@@@@@@                                                                                                                                                    
  @@@@::::::              :              :::::::::       ..    .:::@@@@@@@@@@@                                                                                                                                                    
  @@@@::&&&&##############################&&&&&&&&&&&&&&&&&&&o::::::.::@@@@@@@                                                                                                                                                    
  @@@@::::::::::::::::&&&&&&&&&&&&&&&&&&&&::::::::::::::::::::::::::::::::@@@@                                                                                                                                                    
  @@@@::::::::::::::::::::::::::oooooooooo::::::::::::::::::::::::::::::::@@@@                                                                                                                                                    
  @@@@::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::@@@@                                                                                                                                                    
  @@@@:::::::::::#######:::::::::::::::::::::::::::::::::########:::::::::@@@@                                                                                                                                                    
  @@@@::::::::::####*@###:::::::::::::::::::::::::::::::###@  @######@@@@@@@@@                                                                                                                                                    
  @@@....888&&o@##     ##:::::::::::::::::::::::::::::::##      #####:.....@@@                                                                                                                                                    
  @@@@@@::::::::#@     #8::::::::::::::::::::::::::::::::#&     #::::::::@@@@@                                                                                                                                                    
  @@@@#################################################################@@@@@@@                                                                                                                                                    
  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                                                                                                                                                    
  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                                                                                                                                                    
                                                                                                                                                                                                                                  
                  $$$        $$$$$      $$$$         $$$$     $$$$$                                                                                                                                                               
                  $$$       $$$$$$$     $$$$         $$$$    $$$$$$$
                  $$$       $$$$$$$     $$$$$       $$$$$    $$$$$$$
                  $$$       $$$$$$$      $$$$       $$$$     $$$$$$$
                  $$$      $$$$ $$$$     $$$$$     $$$$$    $$$$ $$$$
                  $$$      $$$$ $$$$      $$$$     $$$$     $$$$ $$$$
                  $$$     $$$$$ $$$$$     $$$$     $$$$    $$$$$ $$$$$
                  $$$     $$$$   $$$$     $$$$$   $$$$$    $$$$   $$$$
                  $$$     $$$$   $$$$      $$$$   $$$$     $$$$   $$$$
                  $$$    $$$$$   $$$$$     $$$$$ $$$$$    $$$$$   $$$$$
                  $$$    $$$$$$$$$$$$$      $$$$ $$$$     $$$$$$$$$$$$$
          $$$$   $$$$    $$$$$$$$$$$$$      $$$$ $$$$     $$$$$$$$$$$$$
          $$$$   $$$$   $$$$$$$$$$$$$$$      $$$$$$$     $$$$$$$$$$$$$$$
          $$$$$ $$$$$   $$$$       $$$$      $$$$$$$     $$$$       $$$$
          $$$$$$$$$$$  $$$$$       $$$$$     $$$$$$$    $$$$$       $$$$$
           $$$$$$$$$   $$$$         $$$$      $$$$$     $$$$         $$$$
            $$$$$$$    $$$$         $$$$      $$$$$     $$$$         $$$$

 :: Food truck info fetcher :: 
┌───────────────────────────────────────┬──────────────────────────────────────┐
│NAME                                   │ADDRESS                               │
├───────────────────────────────────────┼──────────────────────────────────────┤
│Bay Area Dots, LLC                     │567 BAY ST                            │

```


## Implementation thoughts

The goal of fetching the foodtruck information for the current day always is
very simple to achieve, it certainly wouldn't need IoC or AOP, but I decided to
use spring for the following reasons:

1. Spring is very modular and can be lightweight if only the required modules
are used.
2. It has very good/polished/elegant/proven implementations of tools for
several test cases, in our case for consuming a rest API. 
3. Being modular as it is, it would be very easy to improve it, for example
adding security (provide credentials to consume endpoint, per method access
control), reusing this logic (maybe creating an endpoint to serve the
information categorized by food type) or scaling it to consume several other
endpoints feeding a data base would be very easy in the future.
4. Testing is also very easy as the dependencies can be mocked with
annotations.


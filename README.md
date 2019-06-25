# nova4jmt

This is a java-library based on libnova by Liam Girdwood. From the original project page:

> "libnova is a general purpose, double precision, Celestial Mechanics, Astrometry and Astrodynamics library."

See _[libnova.sourceforge.net](https://libnova.sourceforge.net)_

This project is a fork of the java port [novaforjava](https://sourceforge.net/projects/novaforjava/) by Richard van Nieuwenhoven and fixes an issue that occurs when multiple threads use this library in a parallel way.

### Import
#### maven
```xml
<dependency>
  <groupId>de.kah2.zodiac</groupId>
  <artifactId>nova4jmt</artifactId>
  <version>0.15.0.1</version>
</dependency>
```
#### gradle
```groovy
repositories {
    mavenCentral()
}

dependencies {
  compile 'de.kah2.zodiac:nova4jmt:0.15.0.1'
}
```

### See also
* [libZodiac](https://github.com/kahles/libZodiac) - my library to calculate and interpret signs of the zodiac
* [Mondtag](https://github.com/kahles/mondtag) - my free lunar calendar for android

### License
This library is licensed under the [GNU LESSER GENERAL PUBLIC LICENSE](LICENSE.txt)
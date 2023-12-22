# Pandemic Simulator

This project was started during the COVID-19 lockdown as a way to visualize how the virus was spreading, and how social distancing could reduce spread significantly.

<div align="center">
    &nbsp;&nbsp;
    <img width="380" alt="Normal pandemic simulation" src="https://github.com/pxv8780/pandemic-simulator/assets/22942635/0a76bc53-2d49-4b6d-aceb-dac4cdcab482">
    &nbsp;&nbsp;&nbsp;&nbsp;
    <img width="380" alt="Pandemic simulation with social distancing" src="https://github.com/pxv8780/pandemic-simulator/assets/22942635/ced47d3a-c8d8-40da-8213-3dfd151781fe">
    &nbsp;&nbsp;
    <br>
    <p><sup>A running the pandemic simulator with and without social distancing mode</sup></p>
</div>

### Versioning

Successfully tested and run on Java SE 17 (2023-12-22)

### How to use

Compile all the files in the `source` folder with `javac *.java`

Run as `java Pandemulation`

### Details

This project was started in the early summer of 2020. I was bored and needed something to do. Likewise it was a good way to refreshen my Java skills. This project came with a couple of challenges. They were:

- Setting up the Java GUI, it's not hard, but can be tricky
- Handling of collisions with walls and boundaries
- Correctly calculating the motion vectors, so that objects bounce off each other
- Running a thread to render the graphics at a stable frame rate
- Tracking the rate of "sickness" via a curve

All in all, an interesting and fun project.

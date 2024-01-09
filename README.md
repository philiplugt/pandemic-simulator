# Pandemic Simulator

This project was started during the COVID-19 lockdown as a way to visualize how the virus was spreading, and how social distancing could reduce spread significantly.

<div align="center">
    &nbsp;&nbsp;
    <img width="380" alt="Normal pandemic simulation" src="https://github.com/pxv8780/pandemic-simulator/assets/22942635/a8ddf910-c79f-43b3-b014-1c150ee4f56b">
    &nbsp;&nbsp;&nbsp;&nbsp;
    <img width="380" alt="Pandemic simulation with social distancing" src="https://github.com/pxv8780/pandemic-simulator/assets/22942635/aa920814-404d-40cc-aaaf-ef176464a877">
    &nbsp;&nbsp;
    <br>
    <p><sup>A running the pandemic simulator with and without social distancing mode</sup></p>
</div>

### Versioning

Successfully tested and run on Java SE 17 (2024-01-09)

| Version | Date | Notes |
| ------- | ---- | ----- |
| current | 2024-01-09 | Full refactor, cleaner code, smaller files |
| v1 | 2020-05-10 to 2020-06-01 | Original attempt, not bad, but big code blocks |

### How to use

Compile all the files in the `source` folder with `javac *.java`

Run as `java LaunchPandemicSimulator`

### Details

This project was started in the early summer of 2020. I was bored and needed something to do. Likewise it was a good way to refreshen my Java skills. This project came with a couple of challenges. They were:

- Setting up the Java GUI, it's not hard, but can be tricky
- Handling of collisions with walls and boundaries
- Correctly calculating the motion vectors, so that balls bounce off each other
- Running a thread to render the graphics at a stable frame rate
- Tracking the rate of "sickness" via a curve

All in all, an interesting and fun project.

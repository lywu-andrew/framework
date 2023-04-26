<script lang="ts">
  import { framework } from "../lib/ColorFrameworkImpl";
  import { pie, arc } from "d3";

  /**
   * Colors in the framework's output.
   */
  let colors: string[] = framework.getColors();

  /**
   * Data URI of image to display on the side.
   */
  let image: string = framework.getUri();

  /**
   * The color wheel's color data.
   */
  let color_wheel_data = getColorWheelData(colors);

  /**
   * Computes the data to put in the color wheel.
   * @param colors Colors to show in the color wheel
   * @returns Mapping between color and quantity
   */
  function getColorWheelData(colors: String[]) {
    let color_wheel_data = [];
    for (let i = 0; i < colors.length; i++) {
      color_wheel_data.push({
        color: colors[i],
        count: "1",
      });
    }
    return color_wheel_data;
  }

  // pie chart creation
  const width: number = 800;
  const height: number = width;
  const view: number = width + 100;
  const percent: boolean = false;
  const strokeWidth: number = 1;
  const fontSize: number = 25;
  const strokeLinejoin: string = "round";
  const outerRadius: number = Math.min(width, height) * 0.5 - 60;
  const innerRadius: number = 0;
  const labelRadius: number = outerRadius + 70;
  const strokeColorWOR: string = "white";
  const strokeColorWIR: string = "none";
  const stroke: string = innerRadius > 0 ? strokeColorWIR : strokeColorWOR;
  const padAngle: number = stroke === "none" ? 1 / outerRadius : 0;

  let x = Object.keys(color_wheel_data[0])[0];
  let y = Object.keys(color_wheel_data[0])[1];
  let xVals = color_wheel_data.map((el) => el[x]);
  let yVals = color_wheel_data.map((el) => Number(el[y]));
  if (percent) {
    const total = yVals.reduce((a, b) => a + b, 0);
    yVals = yVals.map((el) => el / total);
  }
  let iVals = color_wheel_data.map((el, i) => i);

  let wedges = pie()
    .padAngle(padAngle)
    .sort(null)
    .value((d, i) => yVals[i])(iVals);

  let arcPath = arc().innerRadius(innerRadius).outerRadius(outerRadius);

  let arcLabel = arc().innerRadius(labelRadius).outerRadius(labelRadius);
</script>

<div class="row">
  <div class="column">
    <img src={image} alt="uploaded_image" class="mt-10 ml-10 mr-10 float-left" width={window.innerWidth / 2} />
  </div>
  <div class="column mt-5 ml-20">
    <div class="flex justify-center mb-20">
      <svg
        width={view}
        {height}
        viewBox="{-width / 2} {-view / 2} {width} {view}"
      >
        {#each wedges as wedge, i}
          <path
            fill={colors[i]}
            d={arcPath(wedge)}
            {stroke}
            stroke-width={strokeWidth}
            stroke-linejoin={strokeLinejoin}
          />
          <g
            text-anchor="middle"
            transform="translate({arcLabel.centroid(wedge)})"
          >
            <text font-size={fontSize}>
              <tspan style="color:white">{xVals[i]}</tspan>
            </text>
          </g>
        {/each}
      </svg>
    </div>
  </div>
</div>

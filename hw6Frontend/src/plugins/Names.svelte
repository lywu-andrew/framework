<script lang="ts">
  import { framework } from "../lib/ColorFrameworkImpl";

  /**
    * Colors in the framework's output.
    */
  let colors: string[] = framework.getColors();

  /**
    * Data URI of image to display on the side.
    */
  let image: string = framework.getUri();

  const url = new URL('https://www.thecolorapi.com/id');

  async function getColorNames () {
      return await Promise.all(
          colors.map( async (color) => {

              // Create dictionary of URI parameters for the API request
              const query = {
                hex: color.slice(color.indexOf("#")),
              };

              // Add parameters to URL
              url.search = new URLSearchParams(query).toString();

              // Make call to TheColorAPI
              const response = await fetch( url.toString() );
              const data = await response.json();
              console.log(data.name.value);

              return {
                  'color': color,
                  'name': data.name.value,
              }
          })
      )
  }
</script>

<div class="row">
  <div class="column">
    <img src={framework.getUri()} alt="uploaded_image" class="ml-10 mt-10 mr-10 float-left" height={window.innerHeight / 2} />
  </div>
  <div class="column">
    <br/>
    <p class="mb-2 text-lg text-center text-gray-500">
        Colors with names generated from <a style="color:blue" href="https://www.thecolorapi.com/">The Color API</a>
      </p>
    <div class="grid grid-cols-4 gap-7 mx-28 mb-20">
      {#await getColorNames() then items}
        {#each items as colorName}
          <div class="p-5 rounded-lg" style="background-color: {colorName.color}">
            {#if framework.isDark(colorName.color)}
              <p class="text-center text-white text-sm">{colorName.color}</p>
              <p class="text-center text-white text-sm">{colorName.name}</p>
            {:else}
              <p class="text-center text-black text-sm">{colorName.color}</p>
              <p class="text-center text-black text-sm">{colorName.name}</p>
            {/if}
          </div>
        {/each}
      {:catch error}
        <p>Something went wrong: {error.message}</p>
      {/await}
    </div>
  </div>
</div>

<script lang="ts">
  import Theme from "./lib/Theme.svelte";
  import Data from "./lib/Data.svelte";
  import { framework } from "./lib/ColorFrameworkImpl";

  // import all visual plugins
  const plugins = import.meta.glob("./plugins/*.svelte", { eager: true });
  const pages = Object.keys(plugins).map((plugin) => plugins[plugin]["default"]);
  const plugin_names = Object.keys(plugins).map((plugin) =>
    plugin.replace("./plugins/", "").replace(".svelte", "")
  );

  // Create tabs for default pages
  let activeTabValue = 1;
  let items = [{ label: "Upload", value: 1, component: Data },
               { label: "Theme", value: 2, component: Theme }];

  // Create tabs for plugins
  for (let i: number = 3; i < plugin_names.length + 3; i++) {
    items.push({
      label: plugin_names[i - 3],
      value: i,
      component: pages[i - 3],
    });
  }

  // json data
  $: json = null;
</script>

<main>
  <div class="flex p-2 subpixel-antialiased mt-2 mb-2">
    <span class="ml-28 font-sans text-4xl subpixel-antialiased text-right"
      >Theme Picker</span
    >
    <ul class="flex-grow my-auto mr-24">
      <div class="flex float-right">
        {#each items as item}
          <div
            class={activeTabValue === item.value
              ? "cursor-pointer bg-gray-200 px-7 py-1 rounded-full mx-3"
              : "cursor-pointer px-7 py-1 rounded-full mx-3 hover:bg-gray-300"}
            on:click={() => {
              json = framework.getJson();
              if (json?.output.length) activeTabValue = item.value;
            }}
            on:keydown={() => {}}
          >
            <li>
              <span>{item.label}</span>
            </li>
          </div>
        {/each}
      </div>
    </ul>
  </div>
  {#each items as item}
    {#if activeTabValue === item.value}
        <svelte:component this={item.component} />
    {/if}
  {/each}
</main>

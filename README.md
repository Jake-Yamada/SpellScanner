# SpellScanner
A simple web scraper that reads from a list and puts information about d&amp;d spells into a json

A simple web parser using the GSON and Jsoup libraries. 

It parses d&d spells found on the dnd.arkalseif.info website and outputs a Json file containing information about the spell. The output JSON is compatible with crobi's rpg cards library found https://github.com/crobi/rpg-cards

For organization purposes spell level and level description are passed in per run basis.

Future improvements: Fix parse errors when the input uses atypical formating, read spell level and description automationally, and support automatic trimming or error logs for when spells get too long to fit in a reasonable size text box. 

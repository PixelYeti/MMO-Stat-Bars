# MMO Stat Bars

## Disabling other plugin bars
In order to use this you will probably want to get rid of the other plugins boss bars.

### MCMMO
Modify the file at `plugins/mcMMO/experience.yml` and set Experience_Bars.Enable to `false`:

```
Experience_Bars:
    Enable: false
```

### MMO Core
MMO Core is a bit more nuanced in that you can't directly disable them but can hide the text.
To do this you will want to head to `plugins/MMOCore/messages.yml` and on the line that begins
`exp-notification` you will want to change it to an empty string:
```
exp-notification: ''
```

## Conflicting Professions/Skills
In order to not display more than one boss bar you will want to disable the skill you don't want showing up.

You can disable a skill in mcMMO by removing the permission from all players.
The permission is in the format of `mcmmo.skills.{skill name lower case}` e.g. `mcmmo.skills.woodcutting`

Disabling them in MMOCore requires you to delete the file within `plugins/MMOCore/professions`. So to disable
woodcutting for MMOCore we could delete the `woodcutting.yml` file.

## Config
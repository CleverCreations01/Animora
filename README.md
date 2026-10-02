# Animora

Animora is an animation-first AI video studio.

The product is deliberately not a generic text-to-video wrapper. The planned engine uses persistent project memory for characters, world rules, emotions, camera direction, dialogue and music so longer animations can be directed as one production.

## MVP
- Android Compose app
- Original visual identity
- Prompt and duration controls from 30 seconds to 3 minutes
- Animation style selector
- Local Director timeline prototype
- Codemagic Android release workflow

## Production engine roadmap
The next layer connects the Director timeline to a real animation/rendering provider, persistent project memory, voice and lip-sync, music, continuity checks and final rendering. Provider credentials must stay server-side and never be embedded in the APK.

# SpaceX Dragon Rockets Repository

Building a lean Java library to manage SpaceX Dragon rockets and missions. SOLID, TDD, clean domain logic — the whole deal, but without the circus.

## Scope
This thing keeps track of rockets, missions, their statuses, and how they move through their lifecycle. All in‑memory, all simple, all under control.

## Approach
- **TDD workflow** — tests first, code after.
- **OO design** — small pieces, clean roles, no spaghetti.
- **AI usage** — I use an LLM as a second pair of eyes for structure and test ideas. I make the calls, write the code, and keep it within the rules.

## Current Status
- [x] Project skeleton
- [x] Domain models (Rocket, Mission, Status enums)
- [x] Repository logic
- [x] CI/CD Workflow
- [x] Unit tests
- [ ] Summary reporting

## Assumptions & Design Choices
- Rocket names are unique — they're the IDs.
- Mission names are unique — same deal.
- A rocket can't join a mission if it's already marked `IN_SPACE`.
- Once a mission hits `ENDED`, it's locked. No more assignments.
- Everything lives in memory. Restart the app, start fresh.

## Verified Behaviors (via tests)
- Rejects assigning a rocket that's already tied to a mission.  
- Mission status reacts to the state of its rockets (`IN_PROGRESS`, `PENDING`).  
- `ENDED` missions block new assignments.  
- Duplicate rocket names are rejected.  
- Lookups for unknown rockets or missions fail fast.  
- Summary ordering is stable and covered by tests.

---

*Built for testing.*

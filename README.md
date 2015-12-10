# checkout-kata

Some thoughts on the Kata challenge.

1) attempted to make the addition of rules fairly non-invasive, as long as they can be expressed in terms of a stateful step that takes an input item count and provides a price total, any number of rules could be added.

2) no mention is made of 'cross SKU' offers (for example if you buy 3 pizzas of different flavours you get a discount). This would require a significantly more complex rule system and so would not be wise to just pre-emptively add without discussion of use cases and business value.

3) in a full checkout system the items would be pre validated as they are scanned, not checked at the end, and so the use of an Either type may be overkill. it would probably be perfectly sufficient to just wrap the checkout function in a Try{}

4) use of a state monad, it would be perfectly possible to combine "rules" by just passing a tuple along the list of rules (one tracking how many items are left, and one accumulating the total so far. However I considered the state monad to be a little cleaner and could be reworked to cover much more complex state threading if ever needed, also the fact we sequence and then reduce means we have the sub results from each rule available which could be advantageous if you want to log a breakdown of how offers were taken up etc.

5) would be good to have a running total (just run the algorithm every time) you add an item to the list, in this case its probably sensible to just call checkout after each item is added as the list needs to be reavaluated each time, and there isn't likely to be a significant amount of items in any one shop to need anything more complex.

6) there are no sanity checks that applying an offer results in a lower price than not applying it. again witht the current requirements this feels more like a pre-processing step when rules are configured, not something you want to do every time. Potentially though if the rule set was more complex it may be possible to create scenarios where different inputs could have different minimisation strategies by permuting the rule application order...

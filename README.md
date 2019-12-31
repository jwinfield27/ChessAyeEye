I'm not married to any of the work I've done, if you want to change the names of any classes/ fucntions/ variables so that they're more clear or make more sense to you thats fine. Also tried to comment and document my thoughts for what I intended for each function and class to be, if you'd like me to comment more (or less) let me know.


##Thonks


- Should the Piece class know anything about its moves or just know *what* it is?
    - *If* **Piece** *class is 'dumb' then our AI will be the one who knows each pieces legal moves which I think makes more sense?*
    
- I think **Board** will also deal with setting up the pieces on the right **Square**s to begin. 
- The more I think about this right now I think **Board** shouldn't know many (any?) rules and just know how to set up and record moves. I think it makes sense that if we just tell our AI what the rules are they will play without breaking them... right?? I think **Board** does need to know about special things like check, checkmate, draw (stalemate?).

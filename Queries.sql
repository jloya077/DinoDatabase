select *
from Dinosaur
where d_name = "Ammosaurus";

SELECT d_name
FROM Dinosaur, taxonomy
WHERE t_name = d_name AND t_species = "A. horneri";


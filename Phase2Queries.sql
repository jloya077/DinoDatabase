select * /* Query in JDBC */
from Dinosaur;

select *  /* Query in JDBC */
from Dinosaur
where d_name = "Ammosaurus";

select d_name  /* Query in JDBC */
from Dinosaur, taxonomy
where  t_species = "A. horneri"
and d_name = t_genus;

SELECT h_name ,d_name , MAX(pt_length)  /* Query in JDBC */
FROM Dinosaur, physicalTraits, habitat
WHERE d_dinokey = pt_dinokey AND h_key = d_habkey
GROUP BY h_name
ORDER BY pt_length ASC;

SELECT h_name ,d_name , MAX(pt_weight)  /* Query in JDBC */
FROM Dinosaur, physicalTraits, habitat
WHERE d_dinokey = pt_dinokey AND h_key = d_habkey
and pt_weight != 'unknown'
GROUP BY h_name
ORDER BY pt_weight desc
LIMIT 3;


SELECT d_name, pt_height   /* Query in JDBC */
FROM Dinosaur, physicalTraits
WHERE d_name = pt_name and pt_height != 'unknown' 
group by d_name 
having pt_height > 3;

SELECT t_species
FROM taxonomy, Dinosaur
WHERE t_dinokey = d_dinokey and d_habkey IN (SELECT h_key FROM habitat WHERE h_name = "forest");

Select d_name, pt_body, pt_length, tp_name
From Dinosaur, timeperiod, physicalTraits
Where d_timeperiod = tp_name and d_dinokey = pt_dinokey
And pt_body like "%large%"
Group by d_name,tp_name 
Having pt_length > 2;

select d_name, l_nation
from Dinosaur, location
where l_dinokey like '%21%'
and d_dinokey = 21;

select d_name, l_nation, h_name, tp_yearsAgo  /* Query in JDBC */
from Dinosaur, location, habitat, timeperiod
where l_dinokey like '%21%'
and d_dinokey = 21 and h_key = d_habkey
and d_timeperiod = tp_name;


SELECT COUNT(d_name) /* Query in JDBC */
FROM Dinosaur
WHERE d_name IN (SELECT d_name FROM Dinosaur, habitat
WHERE d_habkey = h_key
AND h_name = "woodland" AND d_diet = "herbivore");

SELECT DISTINCT h_name
FROM habitat , Dinosaur, physicalTraits
WHERE h_key = d_habkey AND d_dinokey = pt_dinokey and pt_class like "%prosauropod";

select sum(timeperiod.tp_yearsAgo)
from Dinosaur, timeperiod, habitat
where d_type = "land" and d_habkey = h_key
and d_timeperiod = tp_name;

SELECT p_name ,p_enunciation, SQ1.maxL /* Query in JDBC */
FROM pronunciation , Dinosaur, (SELECT d_dinokey as maxDino, max(pt_length) as maxL
                                FROM Dinosaur, physicalTraits
                                WHERE pt_dinokey = d_dinokey) as SQ1
WHERE p_name = d_name AND d_dinokey = SQ1.maxDino;

SELECT COUNT(*)  /* Query in JDBC */
FROM (SELECT d_name as dName
FROM Dinosaur, physicalTraits
WHERE d_type like '%sea%' AND d_dinokey = pt_dinokey
GROUP BY d_name
HAVING pt_weight = 'unknown') as SQ1;

SELECT COUNT(*)
FROM (SELECT d_name as dName
FROM Dinosaur, physicalTraits
WHERE d_type like '%sea%' AND d_dinokey = pt_dinokey
GROUP BY d_name
HAVING pt_weight > 300 and pt_weight != 'unknown') as SQ1;

SELECT d_name, pt_length /* Query in JDBC */
FROM Dinosaur, physicalTraits
WHERE d_dinokey = pt_dinokey AND pt_length
BETWEEN 3.5 AND 6.0 AND d_dinokey IN (
SELECT t_dinokey
FROM taxonomy , Dinosaur
WHERE d_dinokey = t_dinokey AND
t_order = 'prolacertiformes')
GROUP BY d_dinokey;

select d_name, tp_name 
from Dinosaur, habitat, timeperiod
where d_habkey = h_key and d_type = 'land'
INTERSECT
select d_name, tp_name
from Dinosaur, fossil, timeperiod, physicalTraits
where  d_timeperiod = tp_name and f_dinokey = d_dinokey
and pt_mouth like '%serrated%' and d_name = pt_name
group by d_name 
having d_diet = 'herbivore';


Select *
From requests 
Where r_name = 'Albertaceratops' and r_updatestatus = 'f';

SELECT d_name, f_fossilData ,f_fossilEvidence
FROM Dinosaur, fossil WHERE d_dinokey = f_dinokey AND
f_fossilEvidence LIKE "%iguana%"
GROUP BY d_name
INTERSECT
SELECT d_name, f_fossilData, f_fossilEvidence
FROM Dinosaur, fossil WHERE d_dinokey = f_dinokey AND
f_fossilData LIKE "%skeleton%"
GROUP BY d_name;














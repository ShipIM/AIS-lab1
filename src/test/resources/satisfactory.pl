resource('limestone').
resource('caterium ore').
resource('copper ore').
resource('raw quartz').
resource('iron ore').
material('concrete').
material('quickwire').
material('silica').
material('iron plate').
material('iron rebar').
material('screw').
material('wire').
material('copper sheet').
material('copper powder').
consumable('caterium ingot').
consumable('copper ingot').
consumable('iron ingot').
consumable('iron rod').
object('industrial railing').
object('melter').

grind('copper ingot', 'copper powder').
melt('copper ore', 'copper ingot').
melt('caterium ore', 'caterium ingot').
melt('iron ore', 'iron ingot').
construct('copper ingot', 'wire').
construct('limestone', 'concrete').
construct('raw quartz', 'silica').
construct('caterium ingot', 'quickwire').
construct('iron ingot', 'iron plate').
construct('iron ingot', 'iron rod').
construct('iron rod', 'iron rebar').
construct('iron rod', 'screw').
build('iron rod', 'industrial railing').
build('iron ore', 'melter').

need('caterium ingot') :- construct('caterium ingot', 'quickwire').
need('copper ingot') :- construct('copper ingot', 'wire'), grind('copper ingot', 'copper powder').
need('iron ingot') :- construct('iron ingot', 'iron plate'), construct('iron ingot', 'iron rod').
need_build('melter') :- (resource('copper ore'), melt('copper ore', 'copper ingot'), need('copper_ingot')); (resource('caterium ore'), melt('caterium ore', 'caterium ingot'), need('caterium_ingot')); (resource('iron ore'), melt('iron ore', 'iron ingot'), need('iron ingot')).
can_build('melter') :- object('melter'), resource('iron ore'), resource('copper ore'), need_build('melter').

% resource('limestone').
% resource('limestone'), (material('concrete'); consumable('caterium ingot')).
% melt(X, 'copper ingot')
% can_build('melter'). Можно построить плавильню. 
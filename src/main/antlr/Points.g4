grammar Points;

points : point+;
point : 'P' '[' NUMBER ',' NUMBER']';
NUMBER: [0-9]+;
WS: [ \n\t\r]+ -> skip;//channel(HIDDEN);
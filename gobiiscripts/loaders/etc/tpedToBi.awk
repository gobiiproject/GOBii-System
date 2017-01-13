#!/bin/nawk -f
{
    printf("%s %s %s %s ",$1,$2,$3,$4); 
    for(i=5;i<=NF;i++) 
	printf("%s%c",($i=="0")?"N":$i,(i==NF)?ORS:((i%2)?"/":OFS));
}


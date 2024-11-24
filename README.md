# Ivana Stefania 332CD

# Tema POO - GwentStone

<div align="center"><img src="https://tenor.com/view/witcher3-gif-9340436.gif" width="500px"></div>

## Implementarea Jocului de Carti
Acest proiect Java implementeaza un joc de carti in care jucatorii pot folosi diverse tipuri de carti cu abilitati unice pentru a concura unul impotriva celuilalt. 
Jocul gestioneaza interactiunile dintre carti, turele jucatorilor si statisticile jocului prin intermediul unei serii de clase si metode.

## Componente Cheie
Card si Subclase
Card: Clasa de baza abstracta pentru toate cartile din joc, gestionand proprietati comune cum ar fi mana, sanatatea si daunele de atac.
MinionCard, HeroCard, SpecialCard: Subclase ale clasei Card care adauga comportamente si proprietati specifice pentru diferite tipuri de carti.

MinionCard: Carti obisnuite cu atac si aparare de baza.

HeroCard: Carti puternice care pot efectua abilitati speciale ce afecteaza tabla de joc.

SpecialCard: Carti cu efecte si abilitati unice.

## Mecanici de Joc
Game: Clasa principala care gestioneaza logica jocului, inclusiv configurarea tablei de joc, procesarea comenzilor utilizatorilor si mentinerea starii jocului.

Player: Reprezinta un jucator in joc, gestionand pachetul de carti si mana jucatorului.

Deck: Gestioneaza o colectie de carti pentru fiecare jucator.

Table: Reprezinta tabla de joc unde sunt plasate cartile in timpul jocului.

## Caracteristici
Model Singleton: Utilizat in clasa Game pentru a asigura ca doar o instanta a jocului este activa.

Procesare de Comenzi: Interpreteaza si executa diverse comenzi care afecteaza starea jocului, cum ar fi plasarea cartilor si utilizarea abilitatilor.

Statistici ale Jocului: Urmareste totalul jocurilor jucate si victoriile pe jucator.

Creare Dinamica a Cartilor: Utilizeaza o metoda de fabrica in clasa Card pentru a crea tipuri specifice de carti pe baza datelor de intrare.

### Descrierea claselor
### Card
Descriere: Clasa de baza abstracta pentru toate cartile din joc. Gestioneaza proprietati comune, cum ar fi mana, atacul, sanatatea, si statusul (inghetat, atacat, tanc).

Proprietati: Mana, atac, sanatate, inghetat, atacat, tanc.

Metode: Constructor care initializeaza o carte cu datele specificate, metoda statica create() care instantiaza subclase specifice in functie de tipul cartii.

### MinionCard
Descriere: Reprezinta cartile de tip minion, derivate din clasa Card.

Proprietati: Toate proprietatile de la clasa Card.
M
etode: Constructor care apeleaza constructorul superclasei.

### HeroCard
Descriere: Reprezinta cartile eroi, oferind abilitati speciale care afecteaza jocul in moduri semnificative. Este o subclasa a Card.

Proprietati: Include un set de sanatate initializat specific pentru eroi.

Metode: ability() care este definita pentru a executa efecte speciale pe randurile de carti.

### SpecialCard
Descriere: O clasa care reprezinta carti cu efecte unice, extinzand MinionCard.

Proprietati si Metode: Poate include metode specifice care suprascriu comportamentul standard al atacurilor sau al interactiunilor.

### Game
Descriere: Clasa principala care coordoneaza logica de joc, gestionand starea jocului, turele si actiunile jucatorilor.

Proprietati: Include elemente cum ar fi tabla de joc, pachetele de carti ale jucatorilor, numarul de jocuri si castigurile.

Metode: Metode pentru initierea jocului, procesarea actiunilor, si gestionarea turelor si a rundelor.

### Player
Descriere: Reprezinta jucatorii in joc, gestionand resursele precum mana si pachetul de carti.

Proprietati: Mana, lista de pachete de carti, numarul de jocuri castigate.

Metode: Metode pentru gestionarea manei si pentru extragerea cartilor din pachet.

### Deck
Descriere: Gestioneaza un pachet de carti pentru un jucator, permitand manipularea cartilor (de exemplu, amestecarea, extragerea).

Proprietati: Lista de carti.

Metode: Metode pentru amestecarea cartilor, extragerea unei carti, verificarea daca pachetul este gol.

### Table
Descriere: Reprezinta tabla de joc unde sunt plasate cartile. Gestioneaza organizarea spatiala a cartilor si interactiunile dintre acestea.

Proprietati: Structura tabelara cu randuri si coloane pentru plasarea cartilor, referinte catre pachetele si mainile jucatorilor.

Metode: Metode pentru adaugarea, inlaturarea si obtinerea cartilor de pe tabel, si pentru determinarea proprietarului unei carti.

#### Aceste clase lucreaza impreuna pentru a crea o experienta de joc complexa si interactiva, fiecare clasa avand responsabilitati clare si bine definite in cadrul jocului.
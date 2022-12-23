# Dokumentacja

## Interfejs

### Układ

Program wykorzystuje TUI (terminal user interface) o układzie zaprezentowanym poniżej.
```
Tracks:
[id:0, filename: output.wav]
[20:04:20.000/69:21:37.123]
[==============================>----------------------------------------------]
*[id:1, filename: abcdef.wav]
[28:02:20.000/69:21:37.123]
[==================================>------------------------------------------]
[id:2, filename: fade-track-02.wav]
[00:01:15.000/00:02:00.000]
[========================================>------------------------------------]
[id:3, filename: cut-track-03.wav]
[00:01:15.000/00:02:00.000]
[========================================>------------------------------------]


Operations:
1. [fade   2    22:22:22.000 69:21:37.123    2]
2. [cut    1    12:34:56.789 69:21:37.123    3]


Command-line:
> help
```

Szerokość interfejsu wynosi 80 znaków lub dopasowana jest do rozmiaru terminala użytkownika. 80 znaków szerokości to często domyślna szerokość terminalu.

### Tracks

W górnej części interfejsu wypisane są ścieżki audio na których operujemy. Każdej ścieżce audio nadane jest ID, a obok niego wyświetlana jest również nazwa pliku, któremu odpowiada ścieżka audio.

Aktualnie wybrana ścieżka audio wyróżniona jest poprzedzającą ją gwiazdką.

Pod blokiem informacji doyczących ID ścieżki audio znajduje się blok zawierający aktualnie wybrany czas dla tej ścieżki (_wskaźnik_ dla danej ścieżki audio) i długość tej ścieżki audio.

Poniżej pokazany jest pasek postępu, który obrazuje przybliżoną pozycję wskaźnika w kontekście całej ścieżki audio.

Ścieżka audio: konkretny plik dźwiękowy otwarty do edycji.
Wskaźnik: punkt w czasie dla danej ścieżki audio, w którym (dla potrzeb innych komend) obecnie się znajdujemy.

Zmiana ścieżki następuje przez wykonanie komendy `checkout`.
Przesunięcie wskaźnika dla wybranej ścieżki audio (zmiana wybranego czasu) następuje poprzez wykonanie komendy `jump`.

Niektóre komendy tworzą nowe ścieżki. Są to: `open`, `copy`, `cut`.

### Operations

W środkowej sekcji interfejsu znajduje się lista operacji wykonanych na ścieżkach.
Każda kolejna operacja opisana jest w osobnym bloku. Zawartość bloku zależy od konkretnej operacji (opisane w osobnej sekcji dokumentacji). Każdy blok poprzedzony jest numerem operacji.

Numer operacji można wykorzystać, aby cofnąć daną operację przez wykonanie komendy `undo`.

Można również przywrócić ostatnio cofniętą operację wykorzystując komendę `redo`.

### Command-line

Command-line, czyli linia poleceń, służy do wprowadzania kolejnych komend przez użytkownika. Komendy pozwalają na realizacje operacji, które opisane są w osobnej sekcji dokumentacji.

Wykonanie niepoprawnej komendy lub niepoprawne zadziałanie komendy powinno zostać zakomunikowane użytkownikowi w tej sekcji interfejsu w postaci przystępnej wiadomości, na przykład: "plik 'abcd.wav' nie istnieje".

Użytkownik może wyświetlić ekran pomocy z opisem poszczególnych komend wykorzystując komendę `help`.

## Obsługiwane formaty
Przewidujemy obsługę następujących formatów:
- wav
- mp3

## Operacje

### Przyjęte koncepcje

Operacje są zapisywane do kolejki i wykonywane zgodnie z kolejnością pojawienia się.

Projekt służy do przechowywania stanu prac nad grupą plików. Może być plikiem XML, zawiera informacje o ścieżkach, stanie i historię operacji.

Przy otwieraniu nowych ścieżek, konwertujemy każdy z obługiwanych formatów na ten sam, wybrany jako roboczy. Dopiero przy eksportowaniu edytowanego projektu, ścieżka jest konwertowana na wyjściowy format, dowolnie wybrany przez użytkownika spośród obsługiwanych.

#### operacje nieodwracalne

Niektóre operacje nie są odwracalne. Do takich operacji należą eksport ścieżki, zapis projektu oraz usunięcie ścieżki.

Usunięcie ścieżki wymaga dodatkowego potwierdzenia przez użytkownika. ID usuniętej ścieżki pozostaje zarezerwowane w obrębie danego projektu (nie można użyć go ponownie w tym samym projekcie).

### Stan
Stan jest wykorzystywany do przechowywania informacji o aktualnej, aktywnej ścieżce audio i wybranym momencie _wskaźniku_). W momencie zmiany ścieżki zerowany jest stan poprzedni, wskaźnik zostaje ustawiony na `00:00:00.000`. W projekcie jest tylko jeden stan.

#### operacje na stanie
Do operacji na stanie należą komendy `checkout` i `jump`.

Komenda `checkout` zmienia ścieżkę roboczą, czyli tą na której pracujemy. Po wywołaniu tej komendy, wszystkie dalej wywołane operacje na ścieżkach będą wykonywanie na ścieżce roboczej. Wywołanie komendy `checkout` powoduje wyzerowanie wszystkich wskaźników na ścieżkach.

Komenda `jump` tworzy wskaźnik na ścieżce, który określa punkt odtwarzania ścieżki.

### Bloki operacji
Obiekty klasy _operacja_ są przechowywane w kolejce.
Każda operacja wykonana na ścieżce pojawia się w historii operacji, wyświetlonej w środkowej części interfejsu. W nawiasach kwadratowych wyświetlone są wszystkie informacje dotyczące danej operacji, w szczególności informacje o ścieżkach i wskaźnikach, których dotyczy operacja.


Wyświetlenie operacji:

|numer|operacja|aktualna ścieżka|timestamp|nowo utworzona ścieżka|
|-|-|-|-|-|


#### operacje na ścieżkach
Należą do nich `cut`,`copy`,`paste`, `fade-in`, `fade-out`, `delete`.

Wywołanie operacji na ścieżkach powoduje dodanie ich do kolejki oraz stworzenie nowej ścieżki, która zawiera zmiany wynikające z wywołanej operacji. Operacje typu [montaż](#Montaż) pozostawiają nas na ścieżce z której została wywołana operacja. Operacje typu [efekty](#Efekty) ustawiają ścieżkę roboczą na nowo powstałą ścieżkę (automatyczne wykonanie komendy `checkout nowaścieżka`).


## Interakcja
Program obsługiwany jest za pomocą poleceń wpisywanych w pole `> `.
Poniżej znajduje się lista wszystkich dostępnych poleceń.

Komendy operują na wybranej ścieżce, domyślnie jest to ścieżka 0.

### Oznaczenia
() - argumenty opcjonalne
[] - argumenty konieczne

### Lista komend

#### Meta

- help - wyświetlenie wszystkich dostępnych poleceń
- exit - wyjście z programu

#### Pliki
- save - zapis projektu
- load - wczytanie projektu
- open [filename] - otwarcie pliku w nowej ścieżce audio
`> open abc.wav` *Otwiera plik abc.wav w nowej ścieżce audio*
- export [filename] - zapisywanie aktualnie wybranej ścieżki audio do pliku
`> export abc.wav` *Eksportuje aktywną ścieżkę audio do pliku abc.wav*

#### Nawigacja

- checkout [id] - zmienia wybraną ścieżkę audio projektu na której pracuje program
`> checkout 1` *Przejście do ścieżki audio o id:1*
- jump [timestamp] - przesunięcie wskaźnika na podany czas
`> jump 00:21:15.666` *Przeskocz do czasu 00:21:15.666 na aktywnej ścieżce audio*
`> jump` *Przeskocz do początku na aktywnej ścieżce audio*


#### Odtwarzanie
- play (duration) - odtwarza ścieżkę przez dany czas
- play - kontynuuje odtwarzanie ścieżki przez 3 minuty
```
> play 450s
> play 3min *Kontynuuj odtwarzanie ścieżki*
> play *Kontynuuj ostatnio odtwarzaną ścieżkę*
```
- stop - natychmiast zatrzymuje odtwarzanie ścieżki

#### Montaż
- cut [start, stop] - wycina dany przedział czasowy aktywnej ścieżki
`> cut 00:21:15.666 00:21:37.999` *Utwórz nową ścieżkę z fragmentu od 00:21:15.666, do 00:21:37.999 aktywnej ścieżki audio*
`> cut - 00:21:37.999` *Utwórz nową ścieżkę z fragmentu od początku do 00:21:37.999*
- copy (start, stop) - kopiuje całą ścieżkę lub dany przedział ścieżki i zapisuje ją w nowej ścieżce audio
- paste [id] - wklejenie ścieżki o podanym id do aktywnej ścieżki audio w wybranym momencie
- delete [id] - usuwanie ścieżki o danym id, i wszystkich operacji do niej przypisanych
- undo (operation) - usuwanie ostatniej lub podanej operacji z kolejki
- redo - ponowne wykonanie ostatnio cofniętej operacji

#### Efekty
- fade-in (start, stop) - zastosowanie efektu fade in
`> fade-in 00:21:15.666 00:21:37.999` *Efekt fade-in od 00:21:15.666, do 00:21:37.999 w wybranej ścieżce*
- fade-out (start, stop) - zastosowanie efektu fade out
`> fade-out 00:21:15.666 00:21:37.999` *Efekt fade-out od 00:21:15.666, do 00:21:37.999 w wybranej ścieżce*


### Opis parametrów

- filename - nazwa pliku, zawiera rozszerzenie
- id - id ścieżki audio, wyrażone liczbą
- timestamp - format hh:mm:ss.xxx - określa konkretny punkt ścieżki audio
- duration - format liczba+suffiks, gdzie suffiksem może być "s" oznaczające sekundy lub "m" oznaczające minuty
- start, stop - format taki sam jak timestamp
- operation - numer operacji, liczba wybrana spośród wyświetlonych na liście operacji

#### Specjalne wartości

Jako timestamp można podać `-`, który w zależności od pozycji w której jest wykorzystany oznacza początek ścieżki lub koniec ścieżki audio.

Podanie `.` jako timestamp oznacza wartość aktualnie wybranego wskaźnika aktywnej ścieżki.

Jump bez argumentu oznacza przejście na początek ścieżki, natomiast podanie `-` określa przejście na koniec ścieżki.

# Przypadki użycia

Przykład użycia systemu do skrócenia długości ścieżki i nałożenia efektu fade-out:
1. Uruchomienie aplikacji
2. `open nazwa.wav`
3. `jump 00:03:33`
4. `cut . -`
5. `fade-out 00:03:20 -`
6. `export wyjscie.wav`
7. Zapis

Przykład użycia systemu do skopiowania fragmentu ze środka ścieżki i dwukrotne doklejenie jej na końcu ścieżki.
1. Uruchomienie aplikacji
2. `open nazwa.wav`
3. `jump 00:10:15`
4. `copy . 00:10:30` skopiuj od teraz do 00:10:30
5. `jump -` skok do końca pliku
6. `paste 1`
8. `paste 1`
9. `export wyjscie.wav`

Przykład użycia systemu do wycięcia dwóch fragmentów (a i b) ze ścieżki i wklejenia ich na początku pliku (a+b+ścieżka).
1. Uruchomienie aplikacji
2. `open nazwa.wav`
3. `jump 00:01:20`
4. `cut . 00:01:30` wycięcie a
5. `jump 00:04:40`
6. `cut . 00:05` wycięcie b
7. `jump 00`
8. `paste 1` wklejenie a
9. `jump 00`
10. `paste 2` wklejenie b
11. `export wyjscie.wav`



### Przykładowe błędy
    - brak pliku projektu
    - uszkodzony plik audio lub projektu
    - błąd konfiguracji urządzenia wyjściowego
    - niepoprawny format
    - Błąd argumentu, podany timestamp jest nieosiągalny, ujemny

#### Przykładowe projekty

- https://github.com/natzoned4004/Sound-Editor/blob/master/SoundEditor.java
- https://docs.oracle.com/javase/8/docs/technotes/guides/sound/programmer_guide/chapter1.html
- https://www.oracle.com/java/technologies/java-sound-api.html
- https://github.com/wassgha/AudioEditorProcessing
- https://github.com/Sciss/Eisenkraut


# Podział klas
![](https://i.imgur.com/GpxG85H.png)

Proponujemy dużą klasę nadrzędną Project, która zawierać będzie obiekty klas odpowiedzialnych za ścieżki (klasa Track), za stan (klasa State), oraz za kolejkę kolejnych operacji (klasa Queue).

Operacjom odpowiadają różne komendy, które może wydać użytkownik. Komedy zdefiniowane są w osobnych klasach. Abstrakcyjna klasa Operation pozwala zaimplementować kolejne operacje na ścieżkach lub na stanie i zebrać całą historię zmian w klasie Queue, czyli kolejce operacji, która należy do projektu.

To oznacza, że klasa Queue i klasa Command będą wykorzystywały abstrakcyjną klasę Operation. Klasy typu Command będą implementowały właściwe działanie na ścieżkach lub stanie. Ścieżki i stan należą do dużej klasy Project.

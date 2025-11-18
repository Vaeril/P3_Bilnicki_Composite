# Wzorce Composite + Visitor

Przykład ilustrujący budowę i wykorzystanie wzorców **Kompozyt** (ang. _Composite_) i **Odwiedzający** (_Visitor_).

**Przy okazji**:
- interakcja z systemem plików
- format Markdown


## Zadanie
Zadanie polega na opracowaniu narzędzia pomagającego przeanalizować strukturę i zawartość plików projektu javowego
(to jedynie przykład).
Ma to być program konsolowy z trybem interaktywnym. Do wyboru: polecenia lub proste menu z opcjami.
Kluczowe wymagania funkcjonalne:

- skanowanie zawartości wskazanego katalogu w systemie plików (odtworzenie struktury drzewa za pomocą wzorca Kompozyt),
- wyświetlanie drzewa (podobnie jak polecenie systemowe `tree`),
- zliczanie plików poszczególnych typów (na bazie rozszerzenia nazwy),
- zliczanie liczby wierszy w plikach wskazanego typu (uproszczona wersja np. narzędzia `cloc`),
- generowanie raportu w postaciu pliku w formacie Markdown.


### Wymagania funkcjonalne i niefunkcjonalne:
**Poziom 3**
- przygotowanie bazy pod wzorzec kompozyt: wspólny interfejs, klasy dla plików i katalogów 
- zaimplementowanie procedury skanującej wskazany katalog (wraz z podkatalogami, rekursywnie)
  i budujący strukturę drzewa z wykorzystaniem wzorca _Kompozyt_
- zaimplementowanie metody wyświetlającej strukturę (z uwzględnieniem wcięć dla kolejnych poziomów)

**Poziom 4**
- zaimplementowanie zliczania plików poszczególnych typów; typy plików najłatwiej zdefiniować za pomocą wzorców nazw
  np. `.+\.java$` odpowiada za pliki źródłowe Javy; trzeba zdefiniować co najmniej trzy typy: pliki źródłowe Javy,
  pliki tekstowe i pliki związane z narzędziem Gradle
- zaimplementowanie zliczania liczby wierszy w plikach wskazanego typu

**Poziom 5**
- zastosowanie wzorca _Odwiedzający_ do generowania raportu w formacie Markdown; ma zostać wygenerowany plik
  np. _raport.md_ zawierający kilka sekcji i sformatowaną zawartość; szczegóły poniżej
- dodanie listy ignorowanych wzorców nazw plików i katalogów (podobnie jak .gitignore)

**Format raportu**

```markdown
# General info
- path: ./test/path
- subdirectory count: 2
- total file count: 5

# Tree
./test/path
- info.txt
- README.md
- src
    - Foo.java
    - pkg
        - Bar.java
        - notes.txt

# Stats
## Files by type
- Text files:
    - /info.txt
    - /README.md
    - src/pkg/notes.txt
- Java source files:
    - src/Foo.java
    - src/pkg/Bar.java
- Gradle files:

## Line count
- Text files: 34
- Java source files: 105
- Gradle files: 0
```


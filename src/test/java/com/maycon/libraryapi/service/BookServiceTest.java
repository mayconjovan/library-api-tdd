package com.maycon.libraryapi.service;

import com.maycon.libraryapi.api.model.entity.Book;
import com.maycon.libraryapi.api.model.repository.BookRepository;
import com.maycon.libraryapi.api.service.BookService;
import com.maycon.libraryapi.api.service.impl.BookServiceImpl;
import com.maycon.libraryapi.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    private BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(repository) {
        };
    }

    @Test
    @DisplayName("Salvar o livro.")
    public void saveBookTest() {
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(book)).thenReturn(
                Book.builder()
                        .id(1l)
                        .isbn("123")
                        .title("As aventuras")
                        .author("Fulano")
                        .build());

        Book savedBook = service.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
    }

    public Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
    }

    @Test
    @DisplayName("Lançar erro de negócio ao tentar salvar um livro com isbn duplicado.")
    public void shouldNotSaveBookWithDuplicatedISBN() {
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(book));
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado.");

        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve obter um livro por id.")
    public void getByIdTest() {
        Long id = 1l;
        Book book = createValidBook();
        book.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = service.getById(id);

        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());

    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um livro por id quando ele não existir na base.")
    public void bookNotFoundByIdTest() {
        Long id = 1l;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> foundBook = service.getById(id);

        assertThat(foundBook.isPresent()).isFalse();

    }

    @Test
    @DisplayName("Deve deletar um livro.")
    public void deleteBookTest() {
        Book book = Book.builder().id(1l).build();

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.delete(book));

        Mockito.verify(repository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar deletar um livro inexistente.")
    public void deleteInvalidBookTest() {
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book));

        Mockito.verify(repository, Mockito.never()).delete(book);

    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar atualizar um livro inexistente.")
    public void updateInvalidBookTest() {
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book));

        Mockito.verify(repository, Mockito.never()).save(book);

    }

    @Test
    @DisplayName("Deve atualizar um livro.")
    public void updateBookTest() {
        long id = 1l;
        Book updatingBook = Book.builder().id(id).build();

        Book updateBook = createValidBook();
        updateBook.setId(id);

        Mockito.when(repository.save(updatingBook)).thenReturn(updateBook);

        Book book = service.update(updatingBook);

        assertThat(book.getId()).isEqualTo(updateBook.getId());
        assertThat(book.getTitle()).isEqualTo(updateBook.getTitle());
        assertThat(book.getIsbn()).isEqualTo(updateBook.getIsbn());
        assertThat(book.getAuthor()).isEqualTo(updateBook.getAuthor());

    }
}

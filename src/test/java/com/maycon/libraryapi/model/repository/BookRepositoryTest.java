package com.maycon.libraryapi.model.repository;

import com.maycon.libraryapi.api.model.entity.Book;
import com.maycon.libraryapi.api.model.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Retorna verdadeiro quando existir um livro na base com o ISBN informado.")

    public void returnTrueWhenIsbnExists() {
        // cenário
        String isbn = "123";
        Book book = Book.builder().title("Aventuras").author("fulano").isbn(isbn).build();
        entityManager.persist(book);

        // execução
        boolean exists = repository.existsByIsbn(isbn);

        // verificação
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Retorna falso quando não existir um livro na base com o ISBN informado.")
    public void returnFalseWhenIsbnDoesntExists() {
        // cenário
        String isbn = "123";

        // execução
        boolean exists = repository.existsByIsbn(isbn);

        // verificação
        assertThat(exists).isFalse();
    }
}

package com.maycon.libraryapi.api.service;


import com.maycon.libraryapi.api.DTO.LoanFilterDTO;
import com.maycon.libraryapi.api.model.entity.Book;
import com.maycon.libraryapi.api.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LoanService {
    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);

    Page<Loan> find(LoanFilterDTO filter, Pageable pageable);

    Page<Loan> getLoansByBook(Book book, Pageable pageable);
}

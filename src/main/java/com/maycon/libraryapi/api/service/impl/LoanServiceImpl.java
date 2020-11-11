package com.maycon.libraryapi.api.service.impl;

import com.maycon.libraryapi.api.DTO.LoanFilterDTO;
import com.maycon.libraryapi.api.model.entity.Book;
import com.maycon.libraryapi.api.model.entity.Loan;
import com.maycon.libraryapi.api.service.LoanService;
import com.maycon.libraryapi.exception.BusinessException;
import com.maycon.libraryapi.api.model.repository.LoanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {
    private LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        if(repository.existsByBookAndNotReturned(loan.getBook())){
            throw new BusinessException("Book already loaned");
        }
        return repository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return repository.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterDTO filter, Pageable pageable) {
        return repository.findByBookIsbnOrCustomer(filter.getIsbn(), filter.getCustomer(), pageable);
    }

    @Override
    public Page<Loan> getLoansByBook(Book book, Pageable pageable) {
        return repository.findByBook(book, pageable);
    }

    @Override
    public List<Loan> getAllLateLoans() {
        final Integer loanDays = 4;
        LocalDate threeDaysAgo = LocalDate.now().minusDays(loanDays);
        return repository.findByLoanDateLessThanAndNotReturned(threeDaysAgo);
    }
}

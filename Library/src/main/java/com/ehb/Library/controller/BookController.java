package com.ehb.Library.controller;

import com.ehb.Library.model.Book;
import com.ehb.Library.model.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class BookController {

    BookDao bookDao;

    @Autowired
    public BookController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @ModelAttribute("allBooks")
    public Iterable<Book> getBooks() {
        return bookDao.findAll();
    }

    @ModelAttribute("modelBook")
    public Book modelBook() {
        return new Book();
    }

//    @GetMapping({"/", "/books"})
//    public String indexPage() {
//        return "index";
//    }

    @GetMapping({"/", "/books"})
    public String indexPage(ModelMap model, String keywords) {
        if (keywords == null) {
            model.addAttribute("books", getBooks());
            return "index";
        }
        model.addAttribute("books", bookDao.filteredBooks(keywords));
        return "index";
    }

    @GetMapping("/addNewBook")
    public String addBookPage() {
        return "newBook";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }

    @PostMapping("/addNewBook")
    public String addBook(@ModelAttribute("modelBook") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.print(bindingResult);
            return "newBook";
        }
        bookDao.save(book);
        return "redirect:books";
    }

    @GetMapping("/detail/{id}")
    public String getBook(ModelMap map, @PathVariable(value = "id") int id) {
        Optional<Book> book = bookDao.findById(id);
        if (book.isPresent()) {
            map.addAttribute("book", book.get());
            return "detail";
        }
        return "redirect:index";
    }

    @GetMapping("/update/book/{id}")
    public String getUpdateBook(ModelMap map, @PathVariable(value = "id") int id) {
        Optional<Book> book = bookDao.findById(id);
        if (book.isPresent()) {
            map.addAttribute("book", book.get());
            return "update";
        }
        return "redirect:index";
    }

//    @PostMapping("/updateBook/{id}")
//    public String postUpdateBook(@PathVariable(value = "id") int id) throws ParseException {
//        Optional<Book> isPresent = bookDao.findById(id);
//        if (isPresent.isPresent()) {
//            Book book = isPresent.get();
//            book.setTitle("One Punch Man");
//            book.setAuthor("Yusuke Murata");
//            book.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2009-01-01"));
//            bookDao.save(book);
//            return "redirect:/";
//        }
//        System.out.print("Error occured");
//        return "update";
//    }

//    @PostMapping("/updateBook/{id}")
//    public String postUpdateBook(@PathVariable(value = "id") int id,
//                                 @RequestParam String title,
//                                 @RequestParam String author,
//                                 @RequestParam String date) throws ParseException {
//        Optional<Book> isPresent = bookDao.findById(id);
//        if (isPresent.isPresent()) {
//            Book updateBook = isPresent.get();
//            updateBook.setTitle(title);
//            updateBook.setAuthor(author);
//            updateBook.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(date));
//            bookDao.save(updateBook);
//            return "redirect:/";
//        }
//        System.out.print("Error occured");
//        return "update";
//    }

    @PostMapping("/updateBook/{id}")
    public String postUpdateBook(@ModelAttribute("modelBook") @Valid Book book,
                                 @PathVariable(value = "id") int id,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            System.out.print("Error occured");
            return "update";
        }
        Optional<Book> isPresent = bookDao.findById(id);
        if (isPresent.isPresent()) {
            Book updateBook = isPresent.get();
            updateBook.setTitle(book.getTitle());
            updateBook.setAuthor(book.getAuthor());
            updateBook.setDate(book.getDate());
            updateBook.setImageUrl(book.getImageUrl());
            bookDao.save(updateBook);
        }
        return "redirect:/";
    }

    @PostMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable(value = "id") int id) {
        Optional<Book> book = bookDao.findById(id);
        if (book.isPresent()) {
            Book deleteBook = book.get();
            bookDao.deleteById(deleteBook.getId());
            return "redirect:/";
        }
        System.out.println("Error occured");
        return "error";
    }


}

package woopaca.jpashop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import woopaca.jpashop.domain.item.Book;
import woopaca.jpashop.domain.item.Item;
import woopaca.jpashop.service.ItemService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/new")
    public String createItemForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/new")
    public String createItem(@ModelAttribute BookForm bookForm, Model model) {
        Book book = Book.builder()
                .name(bookForm.getName())
                .price(bookForm.getPrice())
                .stockQuantity(bookForm.getStockQuantity())
                .author(bookForm.getAuthor())
                .isbn(bookForm.getIsbn())
                .build();

        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping("")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId); // 원래 캐스팅 하는 것은 좋지 않다.
        BookForm form = BookForm.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .stockQuantity(item.getStockQuantity())
                .author(item.getAuthor())
                .isbn(item.getIsbn())
                .build();

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") String itemId, @ModelAttribute BookForm form) {
        Book book = Book.builder()
                .id(form.getId())
                .name(form.getName())
                .price(form.getPrice())
                .stockQuantity(form.getStockQuantity())
                .author(form.getAuthor())
                .isbn(form.getIsbn())
                .build();

        itemService.saveItem(book);
        return "redirect:/items";
    }
}

package woopaca.jpashop.domain.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue(value = "B")
@NoArgsConstructor
@SuperBuilder
public class Book extends Item {

    private String author;
    private String isbn;

    public Book(String name, int price, int stockQuantity, List<Category> categories,
                String author, String isbn) {
        super(name, price, stockQuantity, categories);
        this.author = author;
        this.isbn = isbn;
    }
}

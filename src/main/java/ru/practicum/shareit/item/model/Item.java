package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "items")
public class Item {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Название предмета не может быть пустым!")
    @Column(name = "name")
    private String name;
    @NotBlank(message = "Описание предмета не может быть пустым!")
    @Column(name = "description")
    private String description;
    @Column(name = "available")
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;
    @Transient
    private ItemRequest request;
}

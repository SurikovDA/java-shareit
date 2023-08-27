package ru.practicum.shareit.request.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Generated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Описание не может быть пустым.")
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "requestor_id", referencedColumnName = "id")
    private User requestor;
    @Column(name = "created")
    private LocalDateTime created;
    @JsonIgnore
    @OneToMany(mappedBy = "itemRequest")
    private List<Item> items = new ArrayList<>();
}

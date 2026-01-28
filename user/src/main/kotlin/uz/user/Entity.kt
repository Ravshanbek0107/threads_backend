package uz.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.Date

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class BaseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,
    @CreatedBy var createdBy: Long? = null,
    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false,
)


@Entity
@Table(name = "users")
class User(
    @Column(nullable = false) var username : String,
    @Column(nullable = false) var fullname : String,
    @Column(nullable = false) var bio : String,
    @Column(nullable = false) var password : String,
    @Enumerated(EnumType.STRING) var status : UserStatus = UserStatus.ACTIVE,
    // avatarka
): BaseEntity()

@Entity
@Table(name = "follows",
        uniqueConstraints = [
            UniqueConstraint(columnNames = ["follower_id", "following_id"])
])
class Follow(
    @ManyToOne(fetch = FetchType.LAZY) var follower: User,
    @ManyToOne(fetch = FetchType.LAZY) var following: User,
): BaseEntity()

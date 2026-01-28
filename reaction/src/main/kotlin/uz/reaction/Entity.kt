package uz.reaction

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
@Table(
    name = "reaction_stat",
    uniqueConstraints = [UniqueConstraint(columnNames = ["threadId"])]
)
class ReactionStat(

    @Column(nullable = false) var threadId: Long,

    @Column(nullable = false) var likeCount: Long = 0,

    @Column(nullable = false) var viewCount: Long = 0,

) : BaseEntity()

@Entity
@Table(
    name = "reaction_like",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["threadId", "userId"])
    ]
)
class ReactionLike(

    @Column(nullable = false) var threadId: Long,

    @Column(nullable = false) var userId: Long

) : BaseEntity()

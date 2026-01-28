package uz.thread

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
@Table(name = "threads")
class Thread(

    @Column(nullable = false) var authorId: Long,

    @Enumerated(EnumType.STRING) @Column(nullable = false) var type: ThreadType,

    @Column(columnDefinition = "TEXT") var content: String? = null,

    @ManyToOne(fetch = FetchType.LAZY) var parentThread: Thread? = null,

    //status agar user ozini diactivated qilsa postlari ham korinmasligi uchun yani user umuman yoq bolmaydi bazada qoladi
    @Enumerated(EnumType.STRING) @Column(nullable = false) var status: ThreadStatus = ThreadStatus.ACTIVE,

) : BaseEntity()

@Entity
@Table(name = "thread_media")
class ThreadMedia(
    @ManyToOne(fetch = FetchType.LAZY) var thread: Thread,
    @Column(nullable = false) var hashId: Long
) : BaseEntity()
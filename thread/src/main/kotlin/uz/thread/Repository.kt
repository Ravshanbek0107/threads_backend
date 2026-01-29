package uz.thread

import jakarta.persistence.EntityManager
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    fun findByIdAndDeletedFalse(id: Long): T?
    fun trash(id: Long): T?
    fun trashList(ids: List<Long>): List<T?>
    fun findAllNotDeleted(): List<T>
}


class BaseRepositoryImpl<T : BaseEntity>(
    entityInformation: JpaEntityInformation<T, Long>, entityManager: EntityManager
) : SimpleJpaRepository<T, Long>(entityInformation, entityManager), BaseRepository<T> {

    val isNotDeletedSpecification = Specification<T> { root, _, cb -> cb.equal(root.get<Boolean>("deleted"), false) }

    override fun findByIdAndDeletedFalse(id: Long) = findByIdOrNull(id)?.run { if (deleted) null else this }

    @Transactional
    override fun trash(id: Long): T? = findByIdOrNull(id)?.run {
        deleted  = true
        save(this)
    }

    override fun findAllNotDeleted(): List<T> = findAll(isNotDeletedSpecification)
    override fun trashList(ids: List<Long>): List<T?> = ids.map { trash(it) }
}


@Repository
interface ThreadRepository : BaseRepository<Thread> {

    fun findByIdAndDeletedFalseAndStatus(threadId: Long, status: ThreadStatus): Thread?

    fun findAllByAuthorIdAndDeletedFalseAndStatus(authorId: Long, status: ThreadStatus, pageable: Pageable): Page<Thread>

    fun findAllByDeletedFalseAndStatus(status: ThreadStatus, pageable: Pageable): Page<Thread>

    fun findAllByParentThreadAndDeletedFalseAndStatusAndType(
        parentThread: Thread, status: ThreadStatus,type: ThreadType, pageable: Pageable): Page<Thread>
}

@Repository
interface ThreadMediaRepository : BaseRepository<ThreadMedia> {

    fun findAllByThread(thread: Thread): List<ThreadMedia>

}
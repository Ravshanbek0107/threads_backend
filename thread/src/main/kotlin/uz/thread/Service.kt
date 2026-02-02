package uz.thread

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ThreadService {

    fun create(request: ThreadCreateRequest): ThreadResponse

    fun getOne(id: Long): ThreadResponse

    fun getUserThreads(userId: Long, pageable: Pageable): Page<ThreadResponse>

    fun getAllThreads(pageable: Pageable): Page<ThreadResponse>

    fun getReplies(threadId: Long, pageable: Pageable): Page<ThreadResponse>

    fun delete(id: Long)
}

@Service
class ThreadServiceImpl(
    private val threadRepository: ThreadRepository,
    private val threadMediaRepository: ThreadMediaRepository,
    private val mediaFeignClient: MediaFeignClient,
    private val reactionFeignClient: ReactionFeignClient,
) : ThreadService {

    @Transactional
    override fun create(request: ThreadCreateRequest): ThreadResponse {


        val parentThread = request.parentThreadId?.let {
            threadRepository.findByIdAndDeletedFalseAndStatus(it, ThreadStatus.ACTIVE)
                ?: throw ThreadNotFoundException()
        }

        val thread = Thread(
            authorId = userId(),
            type = request.type,
            content = request.content,
            parentThread = parentThread
        )
        println(userId())

        threadRepository.save(thread)


        request.mediaIds?.let {
            request.mediaIds.forEach {
                threadMediaRepository.save(
                    ThreadMedia(
                        thread = thread,
                        hashId = it
                    )
                )
            }

            mediaFeignClient.setThreadId(
                MediaAttachRequest(
                    ownerId = thread.id!!,
                    hashIds = request.mediaIds
                )
            )
        }

        reactionFeignClient.init(thread.id!!)

        return buildResponse(thread)
    }

    override fun getOne(id: Long): ThreadResponse {

        val thread = threadRepository.findByIdAndDeletedFalseAndStatus(id, ThreadStatus.ACTIVE)
            ?: throw ThreadNotFoundException()

        return buildResponse(thread)
    }

    override fun getUserThreads(userId: Long, pageable: Pageable): Page<ThreadResponse> {

        return threadRepository
            .findAllByAuthorIdAndDeletedFalseAndStatus(userId, ThreadStatus.ACTIVE, pageable)
            .map { buildResponse(it) }
    }

    override fun getAllThreads(pageable: Pageable): Page<ThreadResponse> {

        return threadRepository.findAllByDeletedFalseAndStatus(ThreadStatus.ACTIVE, pageable).map{
            buildResponse(it)
        }
        // shu yerda replylarni qaytarmasligim kerak edi
    }

    override fun getReplies(threadId: Long, pageable: Pageable): Page<ThreadResponse> {

        val parent = threadRepository.findByIdAndDeletedFalseAndStatus(threadId, ThreadStatus.ACTIVE)
            ?: throw ThreadNotFoundException()

        return threadRepository.findAllByParentThreadAndDeletedFalseAndStatusAndType(
            parent, ThreadStatus.ACTIVE, ThreadType.REPLY, pageable)
            .map{
                buildResponse(it)
            }
    }

    @Transactional
    override fun delete(id: Long) {

        //TODO egasiga tekshirish

        val thread = threadRepository.findByIdAndDeletedFalseAndStatus(id, ThreadStatus.ACTIVE)
            ?: throw ThreadNotFoundException()

        thread.status = ThreadStatus.INACTIVE
        thread.deleted = true

        threadRepository.save(thread)









        //keyin like view delete
















    }


    private fun buildResponse(thread: Thread): ThreadResponse {

        val mediaIds = threadMediaRepository.findAllByThread(thread).map {
            it.hashId
        }

        val media = if (mediaIds.isNotEmpty())
            mediaFeignClient.getImagesByHashId(mediaIds)
        else emptyList()

        val reactions = reactionFeignClient.stats(thread.id!!)

        return ThreadResponse(
            id = thread.id!!,
            authorId = thread.authorId,
            type = thread.type,
            parentThreadId = thread.parentThread?.id,
            content = thread.content,
            createdDate = thread.createdDate,
            media = media,
            reactions = reactions
        )
    }
}
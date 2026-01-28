package uz.reaction

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ReactionService {

    fun init(threadId: Long)

    fun LikeOrUnlike(threadId: Long, userId: Long)

    fun view(threadId: Long)

    fun stats(threadId: Long): ReactionStatsResponse
}

@Service
class ReactionServiceImpl(
    private val statRepository: ReactionStatRepository,
    private val likeRepository: ReactionLikeRepository
) : ReactionService {

    @Transactional
    override fun init(threadId: Long) {

        if (statRepository.existsByThreadId(threadId)) return

        statRepository.save(ReactionStat(
                threadId = threadId
            )
        )
    }

    @Transactional
    override fun LikeOrUnlike(threadId: Long, userId: Long) {

        val like = likeRepository.findByThreadIdAndUserId(threadId, userId)

        if (like == null) {
            likeRepository.save(ReactionLike(
                threadId = threadId,
                userId = userId
            )
            )
            statRepository.incrementLike(threadId)
        } else {
            likeRepository.delete(like)
            statRepository.decrementLike(threadId)
        }
    }

    @Transactional
    override fun view(threadId: Long) {
        statRepository.incrementView(threadId)
    }

    override fun stats(threadId: Long): ReactionStatsResponse {

        val stat = statRepository.findByThreadId(threadId) ?: throw StatNotFoundException()

        return ReactionStatsResponse(
            threadId = threadId,
            likeCount = stat.likeCount,
            viewCount = stat.viewCount
        )
    }
}
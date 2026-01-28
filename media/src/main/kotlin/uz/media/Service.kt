package uz.media

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID


interface MediaService {

    fun upload(request: MediaUploadRequest): List<MediaUploadResponse>

    fun setThreadId(request: MediaAttachRequest)

    fun getByHashIds(hashIds: List<Long>): List<MediaDto>
}

@Service
class MediaServiceImpl(
    private val mediaRepository: MediaRepository,

    @Value("\${media.upload:uploads}")
    private val uploadDir: String

) : MediaService {

    private val imageTypes = setOf("image/jpeg", "image/png", "image/webp")

    private val videoTypes = setOf("video/mp4", "video/webm")

    private val maxImageSize = 10L * 1024 * 1024
    private val maxVideoSize = 100L * 1024 * 1024


    @Transactional
    override fun upload(request: MediaUploadRequest): List<MediaUploadResponse> {

        return request.files.map { file ->

            validateFile(file)

            val hashId = generateHashId()

            val extension = file.originalFilename?.substringAfterLast('.', "")

            val uuid = UUID.randomUUID().toString()
            val fileName = "$uuid.$extension"

            val path = Path.of(uploadDir, fileName)

            Files.createDirectories(path.parent)
            file.transferTo(path.toFile())

            val media = Media(
                hashId = hashId,
                originalName = file.originalFilename ?: fileName,
                contentType = file.contentType,
                size = file.size,
                path = path.toString(),

            )

            mediaRepository.save(media)

            MediaUploadResponse(hashId)
        }
    }


    private fun validateFile(file: MultipartFile) {



        val isImage = imageTypes.contains(file.contentType)
        val isVideo = videoTypes.contains(file.contentType)

        if (!isImage && !isVideo) {
            throw UnsupportMediaTypeException()
        }

        if (isImage && file.size > maxImageSize) {
            throw MediaSizeTooLargeException()
        }

        if (isVideo && file.size > maxVideoSize) {
            throw MediaSizeTooLargeException()
        }
    }


    private fun generateHashId(): Long {

        while (true) {
            val hashId = (10000000..99999999).random().toLong()

            if (!mediaRepository.existsByHashId(hashId)) {
                return hashId
            }
        }
    }


    @Transactional
    override fun setThreadId(request: MediaAttachRequest) {

        val medias = mediaRepository.findAllByHashIdIn(request.hashIds)

        medias.forEach {
            it.ownerId = request.ownerId
        }

        mediaRepository.saveAll(medias)
    }


    override fun getByHashIds(hashIds: List<Long>): List<MediaDto> {

        return mediaRepository
            .findAllByHashIdIn(hashIds).map {
                MediaDto(
                    hashId = it.hashId,
                    url = "/api/v1/media/${it.hashId}"
                )
            }
    }
}
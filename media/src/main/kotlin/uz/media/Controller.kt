package uz.media

import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/media")
class MediaController(
    private val mediaService: MediaService
) {

    @PostMapping(
        value = ["/upload"],
        consumes = ["multipart/form-data"]
    )
    fun upload(@RequestPart("files") files: List<MultipartFile>): List<MediaUploadResponse> {

        return mediaService.upload(
            MediaUploadRequest(files)
        )
    }

    @GetMapping("/download/{hashId}")
    fun download(@PathVariable hashId: Long): ResponseEntity<Resource> {

        val file = mediaService.loadFile(hashId)

        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"${file.fileName}\""
            )
            .contentType(MediaType.parseMediaType(file.contentType ?: MediaType.APPLICATION_OCTET_STREAM_VALUE))
            .body(file.resource)
    }

    @PostMapping("/internal/set-thread")
    fun setThreadId(@RequestBody request: MediaAttachRequest) {
        mediaService.setThreadId(request)
    }


    @PostMapping("/internal/get-images")
    fun getByHashIds(@RequestBody hashIds: List<Long>): List<MediaDto> {
        return mediaService.getByHashIds(hashIds)
    }
}
package uz.media

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


    @PostMapping("/internal/set-thread")
    fun setThreadId(@RequestBody request: MediaAttachRequest) {
        mediaService.setThreadId(request)
    }


    @PostMapping("/internal/get-images")
    fun getByHashIds(@RequestBody hashIds: List<Long>): List<MediaDto> {
        return mediaService.getByHashIds(hashIds)
    }
}
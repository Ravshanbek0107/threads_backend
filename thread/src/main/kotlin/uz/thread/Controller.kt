package uz.thread




import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class ThreadController(
    private val threadService: ThreadService
) {

    @PostMapping("/post")
    fun create(@RequestBody request: ThreadCreateRequest): ThreadResponse {
        return threadService.create(request)
    }

    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): ThreadResponse {
        return threadService.getOne(id)
    }

    @GetMapping("/user/{userId}")
    fun getUserThreads(@PathVariable userId: Long, pageable: Pageable): Page<ThreadResponse> {
        return threadService.getUserThreads(userId, pageable)
    }

    @GetMapping("/get-all")
    fun getAll(pageable: Pageable): Page<ThreadResponse> {
        return threadService.getAllThreads(pageable)
    }

    @GetMapping("/replies/{id}")
    fun getReplies(@PathVariable id: Long, pageable: Pageable): Page<ThreadResponse> {
        return threadService.getReplies(id, pageable)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        threadService.delete(id)
    }
}

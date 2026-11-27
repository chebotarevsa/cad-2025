package spring

import ru.bsuedu.cap.hello.impl.MessageProviderImpl
import ru.bsuedu.cap.hello.impl.MessageRendererImpl

beans {
    provider(MessageProviderImpl) {}
    renderer(MessageRendererImpl) {
        messageProvider = ref('provider')
    }
}
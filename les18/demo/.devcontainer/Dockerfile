FROM gradle:8.5-jdk17 as build-env

# Установим необходимые пакеты
USER root
RUN apt-get update && apt-get install -y curl unzip git

# Установка SDKMAN и Maven (опционально)
# RUN curl -s "https://get.sdkman.io" | bash && source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk install maven

# Создаём рабочую папку
WORKDIR /workspace

# Устанавливаем пользователя vscode
ARG USERNAME=vscode
ARG USER_UID=1001
ARG USER_GID=1001
RUN groupadd --gid $USER_GID $USERNAME && \
    useradd --uid $USER_UID --gid $USER_GID -m $USERNAME

USER $USERNAME
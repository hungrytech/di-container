# Spring DI 컨테이너 흉내내보기
- - -
사내 세미나 발표용으로 만든 코드입니다.

Reflection을 활용해서 Bean 자동주입, 수동주입을 간단하게 구현해볼 수 있지 않을까로 시작하였습니다.

### 메타 애너테이션을 어떻게 확인하여 DI시킬 수 있을까?
- DFS, BFS를 활용해서 애너테이션그래프를 탐색하여 구현 


### 동일한 인스턴스는 어떻게 반환할까?
- BeanFactory 구현체 내부의 Map 자료구조를 활용해여 구현 

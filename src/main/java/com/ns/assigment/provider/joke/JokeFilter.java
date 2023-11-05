package com.ns.assigment.provider.joke;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class JokeFilter {

    private boolean sexist;
    private boolean explicit;

    @Override
    public String toString() {
        return "JokeFlags{" +
            "sexist=" + sexist +
            ", explicit=" + explicit +
            '}';
    }
}

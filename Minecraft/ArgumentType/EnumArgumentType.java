package me.mizukiyuu.customsolidsky.utils;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.LiteralText;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class EnumArgumentType implements ArgumentType<Enum<?>> {

    private final Class<? extends Enum<?>> enumClass;
    private final List<String> memberList = new ArrayList<>();

    private EnumArgumentType(final Class<? extends Enum<?>> enumClass) {
        this.enumClass = enumClass;
        Arrays.stream(enumClass.getEnumConstants()).map(s -> s.name().toLowerCase()).forEach(memberList::add);
    }

    public static EnumArgumentType enumArg(Class<? extends Enum<?>> enumClass) {
        return new EnumArgumentType(enumClass);
    }

    public static Enum<?> getEnum(CommandContext<?> context, String name, Enum<?> clazz)
            throws IllegalArgumentException {
        return context.getArgument(name, clazz.getClass());
    }

    @Override
    public Enum<?> parse(StringReader reader) throws CommandSyntaxException {
        final String member = reader.readString();
        if (!memberList.contains(member)) {
            LiteralText message = new LiteralText(
                    "No such member '" + member + "' exists in '" + enumClass.getSimpleName() + "'");
            throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);
        }
        return EnumUtil.valueOf(enumClass, member.toUpperCase());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context,
            final SuggestionsBuilder builder) {
        Arrays.stream(enumClass.getEnumConstants()).forEach(s -> builder.suggest(s.name().toLowerCase()));
        return builder.buildFuture();
    }
}

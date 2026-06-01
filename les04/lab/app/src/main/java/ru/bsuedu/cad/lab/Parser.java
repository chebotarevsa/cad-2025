package ru.bsuedu.cad.lab;
import java.util.*;

public interface Parser
{
    List<Product> parse(List<String> lines);
}
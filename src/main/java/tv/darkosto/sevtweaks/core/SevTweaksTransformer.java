package tv.darkosto.sevtweaks.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import static tv.darkosto.sevtweaks.core.SevTweaksLoadingPlugin.IS_VALID_LIGHT_LEVEL;
import static tv.darkosto.sevtweaks.core.SevTweaksLoadingPlugin.SEVTWEAKS_CORE_LOGGER;

public class SevTweaksTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!transformedName.equals("teamroots.emberroot.entity.wolfdire.EntityDireWolf")) {
            return basicClass;
        }
        
        ClassReader classReader = new ClassReader(basicClass);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        
        SEVTWEAKS_CORE_LOGGER.info("Trying to transform {}", transformedName);
        
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals(IS_VALID_LIGHT_LEVEL)) {
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/monster/EntityMob", IS_VALID_LIGHT_LEVEL, "()Z", false));
                insnList.add(new InsnNode(Opcodes.IRETURN));
                methodNode.instructions.clear();
                methodNode.instructions.insert(insnList);
                SEVTWEAKS_CORE_LOGGER.info("Successful transformation of {}", transformedName);
                break;
            }
        }

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
